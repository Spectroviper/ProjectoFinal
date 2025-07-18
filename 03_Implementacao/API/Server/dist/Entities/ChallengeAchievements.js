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
exports.ChallengeAchievements = void 0;
const typeorm_1 = require("typeorm");
const Achievements_1 = require("./Achievements");
const Challenges_1 = require("./Challenges");
let ChallengeAchievements = class ChallengeAchievements extends typeorm_1.BaseEntity {
};
exports.ChallengeAchievements = ChallengeAchievements;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], ChallengeAchievements.prototype, "ChallengeAchievementId", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Achievements_1.Achievements, achievements => achievements.ChallengeAchievements),
    (0, typeorm_1.JoinColumn)({ name: 'AchievementId' }),
    __metadata("design:type", Achievements_1.Achievements)
], ChallengeAchievements.prototype, "Achievement", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Challenges_1.Challenges, challenges => challenges.ChallengeAchievements),
    (0, typeorm_1.JoinColumn)({ name: 'ChallengeId' }),
    __metadata("design:type", Challenges_1.Challenges)
], ChallengeAchievements.prototype, "Challenge", void 0);
exports.ChallengeAchievements = ChallengeAchievements = __decorate([
    (0, typeorm_1.Entity)(),
    (0, typeorm_1.Unique)(['Achievement', 'Challenge'])
], ChallengeAchievements);
