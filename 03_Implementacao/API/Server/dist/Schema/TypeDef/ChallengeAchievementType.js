"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChallengeAchievementType = void 0;
const graphql_1 = require("graphql");
const AchievementType_1 = require("./AchievementType");
const ChallengeType_1 = require("./ChallengeType");
exports.ChallengeAchievementType = new graphql_1.GraphQLObjectType({
    name: "ChallengeAchievement",
    fields: () => ({
        ChallengeAchievementId: { type: graphql_1.GraphQLID },
        Achievement: { type: AchievementType_1.AchievementType },
        Challenge: { type: ChallengeType_1.ChallengeType }
    })
});
