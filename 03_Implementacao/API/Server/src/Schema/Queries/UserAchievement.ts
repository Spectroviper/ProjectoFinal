import { GraphQLID } from "graphql";
import { UserGameType } from "../TypeDef/UserGameType";
import { appDataSource } from "../../DataSource";
import { UserGames } from "../../Entities/UserGames";
import { UserAchievementType } from "../TypeDef/UserAchievementType";
import { UserAchievements } from "../../Entities/UserAchievements";

export const GET_USER_GAME_BY_USER_GAME_ID = {
  type: UserGameType,
  description: "Returns a UserGame with its related UserAchievements",
  args: {
    UserGameId: { type: GraphQLID }
  },
  async resolve(parent: any, args: any) {
    const { UserGameId } = args;

    const userGame = await appDataSource
      .getRepository(UserGames)
      .createQueryBuilder("ug")
      .leftJoinAndSelect("ug.User", "user")
      .leftJoinAndSelect("ug.Game", "game")
      .leftJoinAndSelect("game.Achievements", "achievement")
      .leftJoinAndSelect(
        "achievement.UserAchievements",
        "userAchievement",
        "userAchievement.UserId = user.UserId"
      )
      .where("ug.UserGameId = :userGameId", { userGameId: UserGameId })
      .getOne();

    if (!userGame) {
      throw new Error("USER GAME DOES NOT EXIST");
    }

    return userGame;
  },
};

export const GET_USER_ACHIEVEMENT_BY_USER_ACHIEVEMENT_ID = {
  type: UserAchievementType,
  description: "Returns a UserAchievement from the BD",
  args: {
    UserAchievementId: { type: GraphQLID }
  },
  async resolve(parent: any, args: any) {
    const { UserAchievementId } = args;

    const userAchievement = await appDataSource
      .getRepository(UserAchievements)
      .createQueryBuilder("ua")
      .leftJoinAndSelect("ua.Achievement", "achievement")
      .where("ua.UserAchievementId = :userAchievementId", { userAchievementId: UserAchievementId })
      .getOne();

    if (!userAchievement) {
      throw new Error("USER ACHIEVEMENT DOES NOT EXIST");
    }

    return userAchievement;
  },
};