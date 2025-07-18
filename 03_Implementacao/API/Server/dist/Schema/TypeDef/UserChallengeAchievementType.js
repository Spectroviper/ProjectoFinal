"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserChallengeAchievementType = void 0;
const graphql_1 = require("graphql");
const Date_1 = require("../CustomTypes/Date");
const AchievementType_1 = require("./AchievementType");
const ChallengeType_1 = require("./ChallengeType");
const UserType_1 = require("./UserType");
exports.UserChallengeAchievementType = new graphql_1.GraphQLObjectType({
    name: "UserChallengeAchievement",
    fields: () => ({
        UserChallengeAchievementId: { type: graphql_1.GraphQLID },
        Clear: { type: graphql_1.GraphQLBoolean },
        UnlockDate: { type: Date_1.CustomDate },
        TotalCollected: { type: graphql_1.GraphQLInt },
        Achievement: { type: AchievementType_1.AchievementType },
        User: { type: UserType_1.UserType },
        Challenge: { type: ChallengeType_1.ChallengeType }
    })
});
