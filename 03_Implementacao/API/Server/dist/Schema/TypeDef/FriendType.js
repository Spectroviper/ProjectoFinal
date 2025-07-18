"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.FriendType = void 0;
const graphql_1 = require("graphql");
const Date_1 = require("../CustomTypes/Date");
const UserType_1 = require("./UserType");
exports.FriendType = new graphql_1.GraphQLObjectType({
    name: "Friend",
    fields: () => ({
        FriendId: { type: graphql_1.GraphQLID },
        State: { type: graphql_1.GraphQLString },
        Date: { type: Date_1.CustomDate },
        UserSender: { type: UserType_1.UserType },
        UserReceiver: { type: UserType_1.UserType }
    })
});
