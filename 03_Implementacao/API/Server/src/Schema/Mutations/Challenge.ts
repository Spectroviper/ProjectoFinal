import { GraphQLID, GraphQLString } from "graphql";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { Challenges } from "../../Entities/Challenges";
import { UserChallenges } from "../../Entities/UserChallenges";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { ChallengeInvites } from "../../Entities/ChallengeInvites";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";
import { appDataSource } from "../../DataSource";

export const CREATE_CHALLENGE = {
  type: MessageType,
  description: "Creates a Challenge in the Database",
  args: {
    UserId:        { type: GraphQLID },
    ChallengeName: { type: GraphQLString },
    Type:          { type: GraphQLString },
    Image:         { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const { UserId, ChallengeName, Type, Image } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager
        .createQueryBuilder(Users, "user")
        .where("user.UserId = :UserId", { UserId })
        .getOne();

      if (!user) {
        throw new Error("USER DOES NOT EXIST!");
      }

      const existing = await manager
        .createQueryBuilder(UserChallenges, "uc")
        .leftJoin("uc.Challenge", "challenge")
        .leftJoin("uc.User", "user")
        .where("user.UserId = :UserId", { UserId })
        .andWhere("challenge.ChallengeName = :ChallengeName", { ChallengeName })
        .getOne();

      if (existing) {
        throw new Error("USER ALREADY HAS A CHALLENGE WITH THIS NAME!");
      }

      const CreatedBy = UserId

      const challenge = await manager
        .createQueryBuilder()
        .insert()
        .into(Challenges)
        .values({
          ChallengeName,
          CreatedBy,
          Type,
          Image,
        })
        .execute();

      const challengeId = challenge.identifiers[0].ChallengeId;

      await manager
        .createQueryBuilder()
        .insert()
        .into(UserChallenges)
        .values({
          User: user,
          Challenge: { ChallengeId: challengeId },
          IsLeader: true,
        })
        .execute();

      return {
        successfull: true,
        message: "CHALLENGE CREATED SUCCESSFULLY",
      };
    });
  },
};


export const UPDATE_CHALLENGE = {
  type: MessageType,
  description: "Updates a Challenge's information in the Database",
  args: {
    ChallengeId:        { type: GraphQLID },
    NewChallengeName:   { type: GraphQLString },
    NewType:            { type: GraphQLString },
    NewImage:           { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeId, NewChallengeName, NewType, NewImage } = args;

    return await appDataSource.transaction(async (manager) => {
      const challenge = await manager
        .createQueryBuilder(Challenges, "challenge")
        .where("challenge.ChallengeId = :ChallengeId", { ChallengeId })
        .getOne();

      if (!challenge) {
        throw new Error("CHALLENGE DOES NOT EXIST!");
      }

      await manager
        .createQueryBuilder()
        .update(Challenges)
        .set({
          ChallengeName: NewChallengeName,
          Type: NewType,
          Image: NewImage,
        })
        .where("ChallengeId = :ChallengeId", { ChallengeId })
        .execute();

      return {
        successfull: true,
        message: "CHALLENGE UPDATED SUCCESSFULLY",
      };
    });
  },
};


export const DELETE_CHALLENGE = {
  type: MessageType,
  description: "Delete a Challenge and its related information from the Database",
  args: {
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const challengeId = args.ChallengeId;
    if (!challengeId) throw new Error("CHALLENGE DOES NOT EXIST");

    const queryRunner = appDataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const existingChallenge = await queryRunner.manager.findOne(Challenges, {
        where: { ChallengeId: challengeId },
      });

      if (!existingChallenge) {
        throw new Error("CHALLENGE NOT FOUND");
      }

      await queryRunner.manager.delete(ChallengeAchievements, {
        Challenge: { ChallengeId: challengeId },
      });

      await queryRunner.manager.delete(UserChallengeAchievements, {
        Challenge: { ChallengeId: challengeId },
      });

      await queryRunner.manager.delete(UserChallenges, {
        Challenge: { ChallengeId: challengeId },
      });

      await queryRunner.manager.delete(ChallengeInvites, {
        Challenge: { ChallengeId: challengeId },
      });

      await queryRunner.manager.delete(Challenges, {
        ChallengeId: challengeId,
      });

      await queryRunner.commitTransaction();

      return { successfull: true, message: "CHALLENGE DELETED SUCCESSFULLY" };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      console.error("FAILED TO DELETE CHALLENGE:", error);
      return { successfull: false, message: "FAILED TO DELETE CHALLENGE." };
    } finally {
      await queryRunner.release();
    }
  },
};


export const START_CHALLENGE = {
  type: MessageType,
  description: "Starts a Challenge and creates ChallengeAchievements for all its members",
  args: {
    UserId: { type: GraphQLID },
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const UserId = parseInt(args.UserId);
    const ChallengeId = parseInt(args.ChallengeId);

    return await appDataSource.transaction(async (manager) => {
      const userChallenge = await manager
        .getRepository(UserChallenges)
        .createQueryBuilder("uc")
        .innerJoinAndSelect("uc.Challenge", "challenge")
        .where("uc.User = :userId", { userId: UserId })
        .andWhere("uc.Challenge = :challengeId", { challengeId: ChallengeId })
        .getOne();

      if (!userChallenge) throw new Error("USER IS NOT PART OF THIS CHALLENGE");
      if (!userChallenge.IsLeader) throw new Error("ONLY THE CHALLENGE LEADER CAN START THE CHALLENGE");

      const challenge = await manager
        .getRepository(Challenges)
        .createQueryBuilder("challenge")
        .leftJoinAndSelect("challenge.ChallengeAchievements", "ca")
        .leftJoinAndSelect("ca.Achievement", "achievement")
        .leftJoinAndSelect("challenge.UserChallenges", "uc")
        .leftJoinAndSelect("uc.User", "user")
        .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
        .getOne();

      if (!challenge) throw new Error("CHALLENGE NOT FOUND");
      if (challenge.ChallengeAchievements.length === 0) {
        throw new Error("CHALLENGE MUST HAVE AT LEAST ONE ACHIEVEMENT TO START");
      }
      if (challenge.StartDate || challenge.IsStarted) {
        throw new Error("CHALLENGE HAS ALREADY STARTED!");
      }

      for (const uc of challenge.UserChallenges) {
        for (const ca of challenge.ChallengeAchievements) {
          const existing = await manager
            .getRepository(UserChallengeAchievements)
            .createQueryBuilder("uca")
            .where("uca.User = :userId", { userId: uc.User.UserId })
            .andWhere("uca.Challenge = :challengeId", { challengeId: ChallengeId })
            .andWhere("uca.Achievement = :achievementId", { achievementId: ca.Achievement.AchievementId })
            .getOne();

          if (existing) {
            await manager
              .getRepository(UserChallengeAchievements)
              .createQueryBuilder()
              .update()
              .set({ Clear: false, UnlockDate: null, TotalCollected: 0 })
              .where("UserChallengeAchievementId = :id", { id: existing.UserChallengeAchievementId })
              .execute();
          } else {
            await manager
              .getRepository(UserChallengeAchievements)
              .createQueryBuilder()
              .insert()
              .values({
                User: { UserId: uc.User.UserId },
                Challenge: { ChallengeId },
                Achievement: { AchievementId: ca.Achievement.AchievementId },
                Clear: false,
                UnlockDate: null,
                TotalCollected: 0,
              })
              .execute();
          }
        }
      }

      await manager
        .getRepository(Challenges)
        .createQueryBuilder()
        .update()
        .set({ StartDate: new Date(), IsStarted: true })
        .where("ChallengeId = :challengeId", { challengeId: ChallengeId })
        .execute();

      return {
        successfull: true,
        message: "CHALLENGE STARTED AND ACHIEVEMENTS INITIALIZED FOR ALL MEMBERS",
      };
    });
  },
};


export const END_CHALLENGE = {
  type: MessageType,
  description: "Ends a challenge, updates users' scores",
  args: {
    ChallengeId: { type: GraphQLID },
  },
  async resolve(_: any, args: any) {
    const { ChallengeId } = args;

    try {
      await endChallengeTransactional(ChallengeId);
      return {
        successfull: true,
        message: "CHALLENGE ENDED AND SCORES UPDATED FOR ALL MEMBERS",
      };
    } catch (error: any) {
      throw new Error("FAILED TO UPDATE CHALLENGE: " + error.message);
    }
  },
};


export async function endChallengeTransactional(challengeId: number) {
  await appDataSource.transaction(async (trx) => {
    const challenge = await trx
      .getRepository(Challenges)
      .createQueryBuilder("challenge")
      .leftJoinAndSelect("challenge.UserChallenges", "userChallenge")
      .leftJoinAndSelect("userChallenge.User", "user")
      .where("challenge.ChallengeId = :challengeId", { challengeId })
      .getOne();

    if (!challenge) throw new Error("CHALLENGE NOT FOUND");
    if (challenge.EndDate) throw new Error("CHALLENGE ALREADY ENDED");

    challenge.EndDate = new Date();
    await trx.getRepository(Challenges).save(challenge);

    for (const userChallenge of challenge.UserChallenges) {
      const userId = userChallenge.User.UserId;

      const clearedAchievements = await trx
        .getRepository(UserChallengeAchievements)
        .createQueryBuilder("uca")
        .where("uca.User = :userId", { userId })
        .andWhere("uca.Challenge = :challengeId", { challengeId })
        .andWhere("uca.Clear = true")
        .getCount();

      if (clearedAchievements > 0) {
        await trx
          .getRepository(Users)
          .createQueryBuilder()
          .update()
          .set({
            TotalPoints: () => `TotalPoints + ${clearedAchievements}`,
          })
          .where("UserId = :userId", { userId })
          .execute();
      }
    }
  });
}