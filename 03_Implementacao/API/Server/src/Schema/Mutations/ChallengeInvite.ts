import { GraphQLBoolean, GraphQLID } from "graphql";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { ChallengeInvites } from "../../Entities/ChallengeInvites";
import { Challenges } from "../../Entities/Challenges";
import { UserChallenges } from "../../Entities/UserChallenges";
import { appDataSource } from "../../DataSource";

export const INVITE_USER_TO_CHALLENGE = {
  type: MessageType,
  description: "Creates an invite or a request for a Challenge",
  args: {
    UserId: { type: GraphQLID },
    ChallengeId: { type: GraphQLID },
    IsRequest: { type: GraphQLBoolean },
  },
  async resolve(parent: any, args: any) {
    const { UserId, ChallengeId, IsRequest } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager.findOne(Users, {
        where: { UserId },
        relations: ["UserChallenges", "UserChallenges.Challenge"],
      });

      if (!user) throw new Error("USER DOES NOT EXIST!");

      const challenge = await manager.findOne(Challenges, {
        where: { ChallengeId },
        relations: ["UserChallenges", "UserChallenges.User"],
      });

      if (!challenge) throw new Error("CHALLENGE DOES NOT EXIST!");

      const isUserInChallenge = user.UserChallenges.some(
        (uc) => uc.Challenge.ChallengeId === ChallengeId
      );

      if (IsRequest) {
        if (isUserInChallenge) {
          throw new Error("USER IS ALREADY PART OF THIS CHALLENGE!");
        }
      } else {
        if (isUserInChallenge) {
          throw new Error("USER IS ALREADY PART OF THIS CHALLENGE!");
        }
      }

      const existingInvite = await manager
        .createQueryBuilder(ChallengeInvites, "invite")
        .leftJoin("invite.User", "user")
        .leftJoin("invite.Challenge", "challenge")
        .where("user.UserId = :userId", { userId: UserId })
        .andWhere("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
        .andWhere("invite.IsRequest = :isRequest", { isRequest: IsRequest })
        .andWhere("invite.State = :state", { state: "Pending" })
        .getOne();

      if (existingInvite) {
        throw new Error("AN INVITE OR REQUEST IS ALREADY PENDING FOR THIS USER AND CHALLENGE!");
      }

      await manager
        .createQueryBuilder()
        .insert()
        .into(ChallengeInvites)
        .values({
          User: { UserId },
          Challenge: { ChallengeId },
          Date: new Date(),
          State: "Pending",
          IsRequest,
        })
        .execute();

      return {
        successfull: true,
        message: IsRequest ? "REQUEST SENT SUCCESSFULLY" : "INVITE SENT SUCCESSFULLY",
      };
    });
  },
};



export const ACCEPT_CHALLENGE_INVITE = {
  type: MessageType,
  description: "Accepts a challenge invite or request",
  args: {
    ChallengeInviteId: { type: GraphQLID },
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeInviteId, UserId } = args;

    return await appDataSource.transaction(async (manager) => {
      const invite = await manager
        .createQueryBuilder(ChallengeInvites, "invite")
        .leftJoinAndSelect("invite.User", "user")
        .leftJoinAndSelect("invite.Challenge", "challenge")
        .where("invite.ChallengeInviteId = :id", { id: ChallengeInviteId })
        .andWhere("invite.State = :state", { state: "Pending" })
        .getOne();

      if (!invite) throw new Error("PENDING INVITE NOT FOUND!");

      const challenge = invite.Challenge;

      if (invite.IsRequest) {
        if (challenge.CreatedBy !== UserId) {
          throw new Error("ONLY THE CHALLENGE OWNER CAN ACCEPT THIS REQUEST");
        }
      } else {
        if (invite.User.UserId !== +UserId) {
          throw new Error("ONLY THE INVITED USER CAN ACCEPT THIS INVITE");
        }
      }

      const alreadyInChallenge = await manager
        .createQueryBuilder(UserChallenges, "uc")
        .where("uc.User = :userId", { userId: invite.User.UserId })
        .andWhere("uc.Challenge = :challengeId", {
          challengeId: challenge.ChallengeId,
        })
        .getOne();

      if (alreadyInChallenge) {
        throw new Error("USER IS ALREADY IN THIS CHALLENGE!");
      }

      await manager
        .createQueryBuilder()
        .update(ChallengeInvites)
        .set({ State: "Accepted" })
        .where("ChallengeInviteId = :id", { id: ChallengeInviteId })
        .execute();

      await manager
        .createQueryBuilder()
        .insert()
        .into(UserChallenges)
        .values({
          User: { UserId: invite.User.UserId },
          Challenge: { ChallengeId: challenge.ChallengeId },
          IsLeader: false,
        })
        .execute();

      return {
        successfull: true,
        message: "CHALLENGE INVITE ACCEPTED SUCCESSFULLY",
      };
    });
  },
};



export const REJECT_CHALLENGE_INVITE = {
  type: MessageType,
  description: "Rejects a challenge invite or request",
  args: {
    ChallengeInviteId: { type: GraphQLID },
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeInviteId, UserId } = args;

    return await appDataSource.transaction(async (manager) => {
      const invite = await manager
        .createQueryBuilder(ChallengeInvites, "invite")
        .leftJoinAndSelect("invite.User", "user")
        .leftJoinAndSelect("invite.Challenge", "challenge")
        .where("invite.ChallengeInviteId = :id", { id: ChallengeInviteId })
        .andWhere("invite.State = :state", { state: "Pending" })
        .getOne();

      if (!invite) throw new Error("PENDING INVITE NOT FOUND!");

      const challenge = invite.Challenge;

      if (invite.IsRequest) {
        if (challenge.CreatedBy !== UserId) {
          throw new Error("ONLY THE CHALLENGE OWNER CAN REJECT THIS REQUEST");
        }
      } else {
        if (invite.User.UserId !== +UserId) {
          throw new Error("ONLY THE INVITED USER CAN REJECT THIS INVITE");
        }
      }

      await manager
        .createQueryBuilder()
        .update(ChallengeInvites)
        .set({ State: "Rejected" })
        .where("ChallengeInviteId = :id", { id: ChallengeInviteId })
        .execute();

      return {
        successfull: true,
        message: "CHALLENGE INVITE REJECTED SUCCESSFULLY",
      };
    });
  },
};


export const DELETE_CHALLENGE_INVITE = {
  type: MessageType,
  description: "Deletes a Challenge Invitation only if it is Accepted or Rejected from the Database",
  args: {
    ChallengeInviteId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeInviteId } = args

    return await appDataSource.transaction(async (manager) => {
      const invite = await manager
        .createQueryBuilder(ChallengeInvites, "invite")
        .where("invite.ChallengeInviteId = :id", { id: ChallengeInviteId })
        .getOne();

      if (!invite) {
        throw new Error("CHALLENGE INVITE NOT FOUND");
      }

      if (invite.State === "Pending") {
        throw new Error("CANNOT DELETE A PENDING INVITE");
      }

      const result = await manager
        .createQueryBuilder()
        .delete()
        .from(ChallengeInvites)
        .where("ChallengeInviteId = :id", { id: ChallengeInviteId })
        .execute();

      if (result.affected === 0) {
        throw new Error("FAILED TO DELETE CHALLENGE INVITE");
      }

      return {
        successfull: true,
        message: "CHALLENGE INVITE DELETED SUCCESSFULLY",
      };
    });
  },
};