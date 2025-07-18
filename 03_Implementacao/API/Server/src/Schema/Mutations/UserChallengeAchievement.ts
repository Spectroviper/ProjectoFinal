import { GraphQLID } from "graphql";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { Challenges } from "../../Entities/Challenges";
import { UserChallenges } from "../../Entities/UserChallenges";
import { endChallengeTransactional } from "./Challenge";
import { appDataSource } from "../../DataSource";

export const LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT = {
  type: MessageType,
  description: "Toggles Clear state of a UserChallengeAchievement and updates average completion",
  args: {
    UserId: { type: GraphQLID },
    UserChallengeAchievementId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId, UserChallengeAchievementId } = args;

    const queryRunner = appDataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const user = await queryRunner.manager
        .createQueryBuilder(Users, "user")
        .leftJoinAndSelect("user.UserChallengeAchievements", "uca")
        .where("user.UserId = :userId", { userId: UserId })
        .getOne();

      if (!user) throw new Error("USER NOT FOUND");

      const userChallengeAchievement = await queryRunner.manager
        .createQueryBuilder(UserChallengeAchievements, "uca")
        .leftJoinAndSelect("uca.Achievement", "achievement")
        .leftJoinAndSelect("uca.User", "user")
        .leftJoinAndSelect("uca.Challenge", "challenge")
        .where("uca.UserChallengeAchievementId = :ucaId", { ucaId: UserChallengeAchievementId })
        .getOne();

      if (!userChallengeAchievement) throw new Error("USER CHALLENGE ACHIEVEMENT NOT FOUND");

      const isRelated = user.UserChallengeAchievements.some(
        (uca) => uca.UserChallengeAchievementId === userChallengeAchievement.UserChallengeAchievementId
      );

      if (!isRelated) throw new Error("USER CHALLENGE ACHIEVEMENT DOES NOT BELONG TO USER");

      userChallengeAchievement.Clear = !userChallengeAchievement.Clear;
      userChallengeAchievement.UnlockDate = userChallengeAchievement.Clear ? new Date() : null;

      await queryRunner.manager.save(userChallengeAchievement);

      const challenge = await queryRunner.manager
        .createQueryBuilder(Challenges, "challenge")
        .leftJoinAndSelect("challenge.ChallengeAchievements", "ca")
        .where("challenge.ChallengeId = :challengeId", { challengeId: userChallengeAchievement.Challenge.ChallengeId })
        .getOne();

      if (!challenge) throw new Error("RELATED CHALLENGE NOT FOUND");

      const userChallenge = await queryRunner.manager
        .createQueryBuilder(UserChallenges, "uc")
        .where("uc.UserId = :userId", { userId: UserId })
        .andWhere("uc.ChallengeId = :challengeId", { challengeId: challenge.ChallengeId })
        .getOne();

      if (!userChallenge) throw new Error("USERCHALLENGE RELATION NOT FOUND");

      const totalAchievements = challenge.ChallengeAchievements.length;

      const unlockedCount = await queryRunner.manager.count(UserChallengeAchievements, {
        where: {
          User: { UserId: UserId },
          Challenge: { ChallengeId: challenge.ChallengeId },
          Clear: true,
        },
      });

      userChallenge.AverageCompletion = totalAchievements === 0
        ? 0
        : Math.floor((unlockedCount / totalAchievements) * 100);

      await queryRunner.manager.save(userChallenge);

      if (userChallenge.AverageCompletion === 100 && !challenge.EndDate) {
        try {
          await endChallengeTransactional(challenge.ChallengeId);
        } catch (error: any) {
          console.error("FAILED TO AUTO END CHALLENGE:", error.message);
        }
      }

      await queryRunner.commitTransaction();

      return {
        successfull: true,
        message: userChallengeAchievement.Clear ? "ACHIEVEMENT UNLOCKED!" : "ACHIEVEMENT LOCKED!",
      };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      console.error("LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT ERROR:", error);
      throw error;
    } finally {
      await queryRunner.release();
    }
  },
};