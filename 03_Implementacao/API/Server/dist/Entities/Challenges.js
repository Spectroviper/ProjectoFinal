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
exports.Challenges = void 0;
const typeorm_1 = require("typeorm");
const ChallengeAchievements_1 = require("./ChallengeAchievements");
const ChallengeInvites_1 = require("./ChallengeInvites");
const UserChallengeAchievements_1 = require("./UserChallengeAchievements");
const UserChallenges_1 = require("./UserChallenges");
let Challenges = class Challenges extends typeorm_1.BaseEntity {
};
exports.Challenges = Challenges;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], Challenges.prototype, "ChallengeId", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Challenges.prototype, "ChallengeName", void 0);
__decorate([
    (0, typeorm_1.Column)({ default: "endfirst" }),
    __metadata("design:type", String)
], Challenges.prototype, "Type", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", Date)
], Challenges.prototype, "StartDate", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", Date)
], Challenges.prototype, "EndDate", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Challenges.prototype, "Image", void 0);
__decorate([
    (0, typeorm_1.Column)({ default: "system" }),
    __metadata("design:type", String)
], Challenges.prototype, "CreatedBy", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserChallenges_1.UserChallenges, UserChallenge => UserChallenge.Challenge),
    __metadata("design:type", Array)
], Challenges.prototype, "UserChallenges", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => ChallengeInvites_1.ChallengeInvites, ChallengeInvite => ChallengeInvite.Challenge, { nullable: true }),
    __metadata("design:type", Array)
], Challenges.prototype, "ChallengeInvites", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserChallengeAchievements_1.UserChallengeAchievements, UserChallengeAchievement => UserChallengeAchievement.Challenge),
    __metadata("design:type", Array)
], Challenges.prototype, "UserChallengeAchievements", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => ChallengeAchievements_1.ChallengeAchievements, ChallengeAchievement => ChallengeAchievement.Challenge),
    __metadata("design:type", Array)
], Challenges.prototype, "ChallengeAchievements", void 0);
exports.Challenges = Challenges = __decorate([
    (0, typeorm_1.Entity)()
], Challenges);
