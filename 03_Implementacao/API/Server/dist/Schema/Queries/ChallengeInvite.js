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
exports.GET_ALL_CHALLENGE_INVITES_BY_CHALLENGEID = exports.GET_ALL_USER_CHALLENGE_INVITES = void 0;
const graphql_1 = require("graphql");
const Users_1 = require("../../Entities/Users");
const ChallengeInviteType_1 = require("../TypeDef/ChallengeInviteType");
const ChallengeInvites_1 = require("../../Entities/ChallengeInvites");
const DataSource_1 = require("../../DataSource");
const Challenges_1 = require("../../Entities/Challenges");
exports.GET_ALL_USER_CHALLENGE_INVITES = {
    type: new graphql_1.GraphQLList(ChallengeInviteType_1.ChallengeInviteType),
    description: "Get all Challenge Invites where the given User is involved (requests or invites)",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            const userExists = yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .where("user.UserId = :id", { id: UserId })
                .getExists();
            if (!userExists) {
                throw new Error("USER DOES NOT EXIST!");
            }
            return yield DataSource_1.appDataSource
                .getRepository(ChallengeInvites_1.ChallengeInvites)
                .createQueryBuilder("invite")
                .leftJoinAndSelect("invite.Challenge", "challenge")
                .leftJoinAndSelect("invite.User", "user")
                .where("user.UserId = :userId", { userId: UserId })
                .orderBy("invite.Date", "DESC")
                .getMany();
        });
    },
};
exports.GET_ALL_CHALLENGE_INVITES_BY_CHALLENGEID = {
    type: new graphql_1.GraphQLList(ChallengeInviteType_1.ChallengeInviteType),
    description: "Get all Challenge Invites related to a specific Challenge",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId } = args;
            const challengeExists = yield DataSource_1.appDataSource
                .getRepository(Challenges_1.Challenges)
                .createQueryBuilder("challenge")
                .where("challenge.ChallengeId = :id", { id: ChallengeId })
                .getExists();
            if (!challengeExists) {
                throw new Error("CHALLENGE DOES NOT EXIST!");
            }
            return yield DataSource_1.appDataSource
                .getRepository(ChallengeInvites_1.ChallengeInvites)
                .createQueryBuilder("invite")
                .leftJoinAndSelect("invite.Challenge", "challenge")
                .leftJoinAndSelect("invite.User", "user")
                .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                .orderBy("invite.Date", "DESC")
                .getMany();
        });
    },
};
