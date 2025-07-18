"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AchievementType = void 0;
const graphql_1 = require("graphql");
const GameType_1 = require("./GameType");
const UserAchievementType_1 = require("./UserAchievementType");
const ChallengeAchievementType_1 = require("./ChallengeAchievementType");
exports.AchievementType = new graphql_1.GraphQLObjectType({
    name: "Achievement",
    fields: () => ({
        AchievementId: { type: graphql_1.GraphQLID },
        AchievementName: { type: graphql_1.GraphQLString },
        About: { type: graphql_1.GraphQLString },
        TotalCollectable: { type: graphql_1.GraphQLInt },
        Image: { type: graphql_1.GraphQLString },
        CreatedBy: { type: graphql_1.GraphQLString },
        Game: { type: GameType_1.GameType },
        ChallengeAchievements: { type: new graphql_1.GraphQLList(ChallengeAchievementType_1.ChallengeAchievementType) },
        UserAchievements: { type: new graphql_1.GraphQLList(UserAchievementType_1.UserAchievementType) }
    })
});
