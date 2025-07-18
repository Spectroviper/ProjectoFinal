"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserChallengeType = void 0;
const graphql_1 = require("graphql");
const ChallengeType_1 = require("./ChallengeType");
const UserType_1 = require("./UserType");
exports.UserChallengeType = new graphql_1.GraphQLObjectType({
    name: "UserChallenge",
    fields: () => ({
        UserChallengeId: { type: graphql_1.GraphQLID },
        IsLeader: { type: graphql_1.GraphQLBoolean },
        AverageCompletion: { type: graphql_1.GraphQLFloat },
        User: { type: UserType_1.UserType },
        Challenge: { type: ChallengeType_1.ChallengeType }
    })
});
