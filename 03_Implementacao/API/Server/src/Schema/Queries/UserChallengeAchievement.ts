import { GraphQLID } from "graphql";
import { appDataSource } from "../../DataSource";
import { UserChallenges } from "../../Entities/UserChallenges";
import { UserChallengeAchievementType } from "../TypeDef/UserChallengeAchievementType";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { DetailedUserChallengeType } from "../TypeDef/DetailedUserChallengeType";

export const GET_USER_CHALLENGE_BY_USER_CHALLENGE_ID = {
  type: DetailedUserChallengeType,
  description: "Gets a specific UserChallenge with its achievements",
  args: {
    UserChallengeId: { type: GraphQLID }
  },
  async resolve(parent: any, args: any) {
    const { UserChallengeId } = args;

    return await appDataSource.transaction(async (manager) => {
      const userChallenge = await manager
        .getRepository(UserChallenges)
        .createQueryBuilder("userChallenge")
        .leftJoinAndSelect("userChallenge.User", "user")
        .leftJoinAndSelect("userChallenge.Challenge", "challenge")
        .where("userChallenge.UserChallengeId = :id", { id: UserChallengeId })
        .getOne();

      if (!userChallenge) {
        throw new Error("USER CHALLENGE DOES NOT EXIST");
      }

      const userChallengeAchievements = await manager
        .getRepository(UserChallengeAchievements)
        .createQueryBuilder("uca")
        .leftJoinAndSelect("uca.Achievement", "achievement")
        .where("uca.UserId = :userId AND uca.ChallengeId = :challengeId", {
          userId: userChallenge.User.UserId,
          challengeId: userChallenge.Challenge.ChallengeId,
        })
        .getMany();

      return {
        UserChallenge: userChallenge,
        UserChallengeAchievements: userChallengeAchievements,
      };
    });
  }
};

export const GET_USER_CHALLENGE_ACHIEVEMENT_BY_USER_CHALLENGE_ACHIEVEMENT_ID = {
  type: UserChallengeAchievementType,
  description: "Returns a UserChallengeAchievement from the DB",
  args: {
    UserChallengeAchievementId: { type: GraphQLID }
  },
  async resolve(parent: any, args: any) {
    const { UserChallengeAchievementId } = args;

    const userChallengeAchievement = await appDataSource
      .getRepository(UserChallengeAchievements)
      .createQueryBuilder("uca")
      .leftJoinAndSelect("uca.Achievement", "achievement")
      .where("uca.UserChallengeAchievementId = :userChallengeAchievementId", { userChallengeAchievementId: UserChallengeAchievementId })
      .getOne();

    if (!userChallengeAchievement) {
      throw new Error("USER CHALLENGE ACHIEVEMENT DOES NOT EXIST");
    }

    return userChallengeAchievement;
  },
};