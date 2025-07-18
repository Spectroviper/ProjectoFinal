"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Users = void 0;
const typeorm_1 = require("typeorm");
const ChallengeInvites_1 = require("./ChallengeInvites");
const UserAchievements_1 = require("./UserAchievements");
const UserChallengeAchievements_1 = require("./UserChallengeAchievements");
const UserChallenges_1 = require("./UserChallenges");
const UserGames_1 = require("./UserGames");
const Friends_1 = require("./Friends");
let Users = class Users extends typeorm_1.BaseEntity {
};
exports.Users = Users;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], Users.prototype, "UserId", void 0);
__decorate([
    (0, typeorm_1.Column)({ unique: true }),
    __metadata("design:type", String)
], Users.prototype, "UserName", void 0);
__decorate([
    (0, typeorm_1.Column)({ unique: true }),
    __metadata("design:type", String)
], Users.prototype, "Email", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Users.prototype, "Biography", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Date)
], Users.prototype, "MemberSince", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Date)
], Users.prototype, "LastLogin", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Number)
], Users.prototype, "TotalPoints", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Number)
], Users.prototype, "AverageCompletion", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Users.prototype, "Image", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserAchievements_1.UserAchievements, UserAchievement => UserAchievement.User, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "UserAchievements", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserChallengeAchievements_1.UserChallengeAchievements, UserChallengeAchievement => UserChallengeAchievement.User, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "UserChallengeAchievements", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserChallenges_1.UserChallenges, UserChallenge => UserChallenge.User, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "UserChallenges", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => ChallengeInvites_1.ChallengeInvites, ChallengeInvite => ChallengeInvite.User, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "ChallengeInvites", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserGames_1.UserGames, UserGame => UserGame.User, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "UserGames", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => Friends_1.Friends, FriendSender => FriendSender.UserSender, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "FriendSenders", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => Friends_1.Friends, FriendReceiver => FriendReceiver.UserReceiver, { nullable: true }),
    __metadata("design:type", Array)
], Users.prototype, "FriendReceivers", void 0);
exports.Users = Users = __decorate([
    (0, typeorm_1.Entity)()
], Users);
