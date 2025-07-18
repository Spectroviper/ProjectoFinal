"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.DetailedUserChallengeType = void 0;
const graphql_1 = require("graphql");
const UserChallengeType_1 = require("./UserChallengeType");
const UserChallengeAchievementType_1 = require("./UserChallengeAchievementType");
exports.DetailedUserChallengeType = new graphql_1.GraphQLObjectType({
    name: "DetailedUserChallenge",
    fields: () => ({
        UserChallenge: { type: UserChallengeType_1.UserChallengeType },
        UserChallengeAchievements: { type: new graphql_1.GraphQLList(UserChallengeAchievementType_1.UserChallengeAchievementType) },
    })
});
