import { GraphQLID } from "graphql";
import { ChallengeAchievementType } from "../TypeDef/ChallengeAchievementType";
import { appDataSource } from "../../DataSource";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";

export const GET_CHALLENGE_ACHIEVEMENT_BY_CHALLENGE_ACHIEVEMENT_ID = {
  type: ChallengeAchievementType,
  description: "Returns a ChallengeAchievement with its related Achievement",
  args: {
    ChallengeAchievementId: { type: GraphQLID }
  },
  async resolve(parent: any, args: any) {
    const { ChallengeAchievementId } = args;

    const challengeAchievement = await appDataSource
      .getRepository(ChallengeAchievements)
      .createQueryBuilder("ca")
      .leftJoinAndSelect("ca.Achievement", "achievement")
      .where("ca.ChallengeAchievementId = :challengeAchievementId", { challengeAchievementId: ChallengeAchievementId })
      .getOne();

    if (!challengeAchievement) {
      throw new Error("CHALLENGE ACHIEVEMENT DOES NOT EXIST");
    }

    return challengeAchievement;
  },
};