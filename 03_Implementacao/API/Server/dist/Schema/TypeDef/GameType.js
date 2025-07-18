"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.GameType = void 0;
const graphql_1 = require("graphql");
const Date_1 = require("../CustomTypes/Date");
const AchievementType_1 = require("./AchievementType");
const UserGameType_1 = require("./UserGameType");
exports.GameType = new graphql_1.GraphQLObjectType({
    name: "Game",
    fields: () => ({
        GameId: { type: graphql_1.GraphQLID },
        GameName: { type: graphql_1.GraphQLString },
        About: { type: graphql_1.GraphQLString },
        Console: { type: graphql_1.GraphQLString },
        Developer: { type: graphql_1.GraphQLString },
        Publisher: { type: graphql_1.GraphQLString },
        Genre: { type: graphql_1.GraphQLString },
        ReleaseDate: { type: Date_1.CustomDate },
        Image: { type: graphql_1.GraphQLString },
        CreatedBy: { type: graphql_1.GraphQLString },
        UserGames: { type: new graphql_1.GraphQLList(UserGameType_1.UserGameType) },
        Achievements: { type: new graphql_1.GraphQLList(AchievementType_1.AchievementType) }
    })
});
