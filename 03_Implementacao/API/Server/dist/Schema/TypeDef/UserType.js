"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserType = void 0;
const graphql_1 = require("graphql");
const Date_1 = require("../CustomTypes/Date");
const ChallengeInviteType_1 = require("./ChallengeInviteType");
const UserAchievementType_1 = require("./UserAchievementType");
const UserChallengeAchievementType_1 = require("./UserChallengeAchievementType");
const UserChallengeType_1 = require("./UserChallengeType");
const UserGameType_1 = require("./UserGameType");
const FriendType_1 = require("./FriendType");
exports.UserType = new graphql_1.GraphQLObjectType({
    name: "User",
    description: "A User in the Application",
    fields: () => ({
        UserId: { type: graphql_1.GraphQLID },
        UserName: { type: graphql_1.GraphQLString },
        Email: { type: graphql_1.GraphQLString },
        Biography: { type: graphql_1.GraphQLString },
        MemberSince: { type: Date_1.CustomDate },
        LastLogin: { type: Date_1.CustomDate },
        TotalPoints: { type: graphql_1.GraphQLInt },
        AverageCompletion: { type: graphql_1.GraphQLInt },
        Image: { type: graphql_1.GraphQLString },
        UserGames: { type: new graphql_1.GraphQLList(UserGameType_1.UserGameType) },
        UserAchievements: { type: new graphql_1.GraphQLList(UserAchievementType_1.UserAchievementType),
            resolve: (parent) => __awaiter(void 0, void 0, void 0, function* () {
                return parent.UserAchievements || [];
            }),
        },
        UserChallengeAchievements: { type: new graphql_1.GraphQLList(UserChallengeAchievementType_1.UserChallengeAchievementType),
            resolve: (parent) => __awaiter(void 0, void 0, void 0, function* () {
                return parent.UserChallengeAchievements || [];
            }),
        },
        UserChallenges: { type: new graphql_1.GraphQLList(UserChallengeType_1.UserChallengeType), },
        ChallengeInvites: { type: new graphql_1.GraphQLList(ChallengeInviteType_1.ChallengeInviteType) },
        FriendSenders: { type: new graphql_1.GraphQLList(FriendType_1.FriendType) },
        FriendReceivers: { type: new graphql_1.GraphQLList(FriendType_1.FriendType) }
    })
});
