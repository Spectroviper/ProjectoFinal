"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChallengeType = void 0;
const graphql_1 = require("graphql");
const ChallengeInviteType_1 = require("./ChallengeInviteType");
const Date_1 = require("../CustomTypes/Date");
const ChallengeAchievementType_1 = require("./ChallengeAchievementType");
const UserChallengeType_1 = require("./UserChallengeType");
exports.ChallengeType = new graphql_1.GraphQLObjectType({
    name: "Challenge",
    fields: () => ({
        ChallengeId: { type: graphql_1.GraphQLID },
        ChallengeName: { type: graphql_1.GraphQLString },
        Type: { type: graphql_1.GraphQLString },
        StartDate: { type: Date_1.CustomDate },
        EndDate: { type: Date_1.CustomDate },
        Image: { type: graphql_1.GraphQLString },
        CreatedBy: { type: graphql_1.GraphQLString },
        UserChallenges: { type: new graphql_1.GraphQLList(UserChallengeType_1.UserChallengeType), },
        ChallengeAchievements: { type: new graphql_1.GraphQLList(ChallengeAchievementType_1.ChallengeAchievementType) },
        ChallengeInvites: { type: new graphql_1.GraphQLList(ChallengeInviteType_1.ChallengeInviteType) }
    })
});
