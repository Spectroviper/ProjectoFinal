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
exports.Achievements = void 0;
const typeorm_1 = require("typeorm");
const ChallengeAchievements_1 = require("./ChallengeAchievements");
const Games_1 = require("./Games");
const UserAchievements_1 = require("./UserAchievements");
const UserChallengeAchievements_1 = require("./UserChallengeAchievements");
let Achievements = class Achievements extends typeorm_1.BaseEntity {
};
exports.Achievements = Achievements;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], Achievements.prototype, "AchievementId", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Achievements.prototype, "AchievementName", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Achievements.prototype, "About", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", Number)
], Achievements.prototype, "TotalCollectable", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Achievements.prototype, "Image", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Achievements.prototype, "CreatedBy", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Games_1.Games, games => games.Achievements),
    __metadata("design:type", Games_1.Games)
], Achievements.prototype, "Game", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserAchievements_1.UserAchievements, UserAchievement => UserAchievement.Achievement, { nullable: true }),
    __metadata("design:type", Array)
], Achievements.prototype, "UserAchievements", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => ChallengeAchievements_1.ChallengeAchievements, ChallengeAchievement => ChallengeAchievement.Achievement, { nullable: true }),
    __metadata("design:type", Array)
], Achievements.prototype, "ChallengeAchievements", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserChallengeAchievements_1.UserChallengeAchievements, UserChallengeAchievement => UserChallengeAchievement.Achievement, { nullable: true }),
    __metadata("design:type", Array)
], Achievements.prototype, "UserChallengeAchievements", void 0);
exports.Achievements = Achievements = __decorate([
    (0, typeorm_1.Entity)()
], Achievements);
