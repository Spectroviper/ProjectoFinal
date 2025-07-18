"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChallengeInviteType = void 0;
const graphql_1 = require("graphql");
const Date_1 = require("../CustomTypes/Date");
const ChallengeType_1 = require("./ChallengeType");
const UserType_1 = require("./UserType");
exports.ChallengeInviteType = new graphql_1.GraphQLObjectType({
    name: "ChallengeInvite",
    fields: () => ({
        ChallengeInviteId: { type: graphql_1.GraphQLID },
        State: { type: graphql_1.GraphQLString },
        Date: { type: Date_1.CustomDate },
        IsRequest: { type: graphql_1.GraphQLBoolean },
        User: { type: UserType_1.UserType },
        Challenge: { type: ChallengeType_1.ChallengeType }
    })
});
