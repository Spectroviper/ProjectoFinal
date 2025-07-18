import { GraphQLID, GraphQLList, GraphQLString } from "graphql";
import { Achievements } from "../../Entities/Achievements";
import { AchievementType } from "../TypeDef/AchievementType";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";
import { appDataSource } from "../../DataSource";

export const GET_ACHIEVEMENT_BY_ACHIEVEMENTID = {
  type: AchievementType,
  description: "Get a certain Achievement in the Database",
  args: {
    AchievementId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { AchievementId } = args;

    const achievement = await appDataSource
      .getRepository(Achievements)
      .createQueryBuilder("achievement")
      .where("achievement.AchievementId = :id", { id: AchievementId })
      .getOne();

    if (achievement) {
      return achievement;
    } else {
      throw new Error("ACHIEVEMENT DOES NOT EXIST!");
    }
  },
};


export const GET_ALL_ACHIEVEMENTS_NOT_IN_A_CHALLENGE = {
  type: new GraphQLList(AchievementType),
  description: "Get all Achievements that are NOT in a specific Challenge",
  args: {
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeId } = args;

    const subQuery = appDataSource
      .getRepository(ChallengeAchievements)
      .createQueryBuilder('ca')
      .select('ca.Achievement')
      .where('ca.Challenge = :challengeId', { challengeId: ChallengeId });

    const achievements = await appDataSource
      .getRepository(Achievements)
      .createQueryBuilder('a')
      .where(`a.AchievementId NOT IN (${subQuery.getQuery()})`)
      .setParameters(subQuery.getParameters())
      .getMany();

    return achievements;
  },
};


export const SEARCH_ACHIEVEMENTS = {
  type: new GraphQLList(AchievementType),
  description:
    "Search Achievements by name and game, excluding those already in a specific challenge",
  args: {
    AchievementName: { type: GraphQLString },
    GameId: { type: GraphQLID },
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { AchievementName, GameId, ChallengeId } = args;

    try {
      const subQuery = appDataSource
        .getRepository(ChallengeAchievements)
        .createQueryBuilder('ca')
        .select('ca.Achievement')
        .where('ca.Challenge = :challengeId', { challengeId: ChallengeId });

      const query = appDataSource
        .getRepository(Achievements)
        .createQueryBuilder('achievement')
        .leftJoinAndSelect('achievement.Game', 'game')
        .where(`achievement.AchievementId NOT IN (${subQuery.getQuery()})`)
        .setParameters(subQuery.getParameters());

      if (AchievementName) {
        query.andWhere('LOWER(achievement.AchievementName) LIKE LOWER(:name)', {
          name: `%${AchievementName}%`,
        });
      }

      if (GameId) {
        query.andWhere('game.GameId = :gameId', { gameId: GameId });
      }

      const achievements = await query.getMany();
      return achievements;
    } catch (err) {
      console.error('Search achievements failed:', err);
      throw new Error('Failed to search achievements.');
    }
  },
};