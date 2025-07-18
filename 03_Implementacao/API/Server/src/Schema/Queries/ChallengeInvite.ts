import { GraphQLID, GraphQLList } from "graphql";
import { Users } from "../../Entities/Users";
import { ChallengeInviteType } from "../TypeDef/ChallengeInviteType";
import { ChallengeInvites } from "../../Entities/ChallengeInvites";
import { appDataSource } from "../../DataSource";
import { Challenges } from "../../Entities/Challenges";

export const GET_ALL_USER_CHALLENGE_INVITES = {
  type: new GraphQLList(ChallengeInviteType),
  description: "Get all Challenge Invites where the given User is involved (requests or invites)",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    const userExists = await appDataSource
      .getRepository(Users)
      .createQueryBuilder("user")
      .where("user.UserId = :id", { id: UserId })
      .getExists();

    if (!userExists) {
      throw new Error("USER DOES NOT EXIST!");
    }

    return await appDataSource
      .getRepository(ChallengeInvites)
      .createQueryBuilder("invite")
      .leftJoinAndSelect("invite.Challenge", "challenge")
      .leftJoinAndSelect("invite.User", "user")
      .where("user.UserId = :userId", { userId: UserId })
      .orderBy("invite.Date", "DESC")
      .getMany();
  },
};

export const GET_ALL_CHALLENGE_INVITES_BY_CHALLENGEID = {
  type: new GraphQLList(ChallengeInviteType),
  description: "Get all Challenge Invites related to a specific Challenge",
  args: {
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeId } = args;

    const challengeExists = await appDataSource
      .getRepository(Challenges)
      .createQueryBuilder("challenge")
      .where("challenge.ChallengeId = :id", { id: ChallengeId })
      .getExists();

    if (!challengeExists) {
      throw new Error("CHALLENGE DOES NOT EXIST!");
    }

    return await appDataSource
      .getRepository(ChallengeInvites)
      .createQueryBuilder("invite")
      .leftJoinAndSelect("invite.Challenge", "challenge")
      .leftJoinAndSelect("invite.User", "user")
      .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
      .orderBy("invite.Date", "DESC")
      .getMany();
  },
};