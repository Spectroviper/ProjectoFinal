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
exports.UserChallenges = void 0;
const typeorm_1 = require("typeorm");
const Challenges_1 = require("./Challenges");
const Users_1 = require("./Users");
let UserChallenges = class UserChallenges extends typeorm_1.BaseEntity {
};
exports.UserChallenges = UserChallenges;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], UserChallenges.prototype, "UserChallengeId", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Boolean)
], UserChallenges.prototype, "IsLeader", void 0);
__decorate([
    (0, typeorm_1.Column)({ type: 'float', default: 0 }),
    __metadata("design:type", Number)
], UserChallenges.prototype, "AverageCompletion", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Challenges_1.Challenges, challenges => challenges.UserChallenges),
    (0, typeorm_1.JoinColumn)({ name: 'ChallengeId' }),
    __metadata("design:type", Challenges_1.Challenges)
], UserChallenges.prototype, "Challenge", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Users_1.Users, users => users.UserChallenges),
    (0, typeorm_1.JoinColumn)({ name: 'UserId' }),
    __metadata("design:type", Users_1.Users)
], UserChallenges.prototype, "User", void 0);
exports.UserChallenges = UserChallenges = __decorate([
    (0, typeorm_1.Entity)(),
    (0, typeorm_1.Unique)(['Challenge', 'User'])
], UserChallenges);
