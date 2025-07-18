"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.SEARCH_GAMES = exports.GET_GAME_BY_GAMEID = exports.GET_ALL_GAMES = void 0;
const graphql_1 = require("graphql");
const Games_1 = require("../../Entities/Games");
const GameType_1 = require("../TypeDef/GameType");
const DataSource_1 = require("../../DataSource");
exports.GET_ALL_GAMES = {
    type: new graphql_1.GraphQLList(GameType_1.GameType),
    description: "Get all Games in the database",
    resolve() {
        return __awaiter(this, void 0, void 0, function* () {
            return yield DataSource_1.appDataSource
                .getRepository(Games_1.Games)
                .createQueryBuilder('game')
                .getMany();
        });
    }
};
exports.GET_GAME_BY_GAMEID = {
    type: GameType_1.GameType,
    description: "Get a certain Game in the Database",
    args: {
        GameId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { GameId } = args;
            const game = yield DataSource_1.appDataSource
                .getRepository(Games_1.Games)
                .createQueryBuilder('game')
                .leftJoinAndSelect('game.Achievements', 'achievement')
                .where('game.GameId = :id', { id: GameId })
                .getOne();
            if (game) {
                return game;
            }
            else {
                throw new Error("GAME DOES NOT EXIST!");
            }
        });
    }
};
exports.SEARCH_GAMES = {
    type: new graphql_1.GraphQLList(GameType_1.GameType),
    description: "Search Games by GameName, Console, Developer, Publisher, and Genre",
    args: {
        GameName: { type: graphql_1.GraphQLString },
        Console: { type: graphql_1.GraphQLString },
        Developer: { type: graphql_1.GraphQLString },
        Publisher: { type: graphql_1.GraphQLString },
        Genre: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const query = DataSource_1.appDataSource
                    .createQueryBuilder(Games_1.Games, 'game');
                const filters = {
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
                return yield query.getMany();
            }
            catch (err) {
                console.error('SEARCH FAILED:', err);
                throw new Error('FAILED TO SEARCH GAMES.');
            }
        });
    },
};
