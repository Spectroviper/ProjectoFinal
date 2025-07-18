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
exports.UserAchievements = void 0;
const typeorm_1 = require("typeorm");
const Achievements_1 = require("./Achievements");
const Users_1 = require("./Users");
let UserAchievements = class UserAchievements extends typeorm_1.BaseEntity {
};
exports.UserAchievements = UserAchievements;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], UserAchievements.prototype, "UserAchievementId", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Boolean)
], UserAchievements.prototype, "Clear", void 0);
__decorate([
    (0, typeorm_1.Column)({ type: 'datetime', nullable: true }),
    __metadata("design:type", Object)
], UserAchievements.prototype, "UnlockDate", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", Number)
], UserAchievements.prototype, "TotalCollected", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Achievements_1.Achievements, achievements => achievements.UserAchievements),
    (0, typeorm_1.JoinColumn)({ name: 'AchievementId' }),
    __metadata("design:type", Achievements_1.Achievements)
], UserAchievements.prototype, "Achievement", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Users_1.Users, users => users.UserAchievements),
    (0, typeorm_1.JoinColumn)({ name: 'UserId' }),
    __metadata("design:type", Users_1.Users)
], UserAchievements.prototype, "User", void 0);
exports.UserAchievements = UserAchievements = __decorate([
    (0, typeorm_1.Entity)(),
    (0, typeorm_1.Unique)(['Achievement', 'User'])
], UserAchievements);
