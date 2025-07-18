"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserGameType = void 0;
const graphql_1 = require("graphql");
const GameType_1 = require("./GameType");
const UserType_1 = require("./UserType");
exports.UserGameType = new graphql_1.GraphQLObjectType({
    name: "UserGame",
    fields: () => ({
        UserGameId: { type: graphql_1.GraphQLID },
        AverageCompletion: { type: graphql_1.GraphQLFloat },
        User: { type: UserType_1.UserType },
        Game: { type: GameType_1.GameType }
    })
});
