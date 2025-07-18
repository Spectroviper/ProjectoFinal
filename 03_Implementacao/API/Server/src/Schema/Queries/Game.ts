import { GraphQLID, GraphQLList, GraphQLString } from 'graphql'
import { Games } from '../../Entities/Games';
import { GameType } from '../TypeDef/GameType';
import { appDataSource } from "../../DataSource";

export const GET_ALL_GAMES = {
  type: new GraphQLList(GameType),
  description: "Get all Games in the database",
  async resolve() {
    return await appDataSource
      .getRepository(Games)
      .createQueryBuilder('game')
      .getMany();
  }
};

export const GET_GAME_BY_GAMEID = {
  type: GameType,
  description: "Get a certain Game in the Database",
  args: {
    GameId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { GameId } = args;
    const game = await appDataSource
      .getRepository(Games)
      .createQueryBuilder('game')
      .leftJoinAndSelect('game.Achievements', 'achievement')
      .where('game.GameId = :id', { id: GameId })
      .getOne();

    if (game) {
      return game;
    } else {
      throw new Error("GAME DOES NOT EXIST!");
    }
  }
};

export const SEARCH_GAMES = {
  type: new GraphQLList(GameType),
  description: "Search Games by GameName, Console, Developer, Publisher, and Genre",
  args: {
    GameName: { type: GraphQLString },
    Console: { type: GraphQLString },
    Developer: { type: GraphQLString },
    Publisher: { type: GraphQLString },
    Genre: { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    try {
      const query = appDataSource
        .createQueryBuilder(Games, 'game');

      const filters: Record<string, string> = {
        GameName: 'game.GameName',
        Console: 'game.Console',
        Developer: 'game.Developer',
        Publisher: 'game.Publisher',
        Genre: 'game.Genre',
      };

      for (const [argKey, column] of Object.entries(filters)) {
        const value = args[argKey];
        if (value) {
          query.andWhere(`LOWER(${column}) LIKE LOWER(:${argKey})`, {
            [argKey]: `%${value}%`,
          });
        }
      }

      return await query.getMany();
    } catch (err) {
      console.error('SEARCH FAILED:', err);
      throw new Error('FAILED TO SEARCH GAMES.');
    }
  },
};
