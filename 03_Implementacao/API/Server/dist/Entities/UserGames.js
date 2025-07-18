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
exports.UserGames = void 0;
const typeorm_1 = require("typeorm");
const Games_1 = require("./Games");
const Users_1 = require("./Users");
let UserGames = class UserGames extends typeorm_1.BaseEntity {
};
exports.UserGames = UserGames;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], UserGames.prototype, "UserGameId", void 0);
__decorate([
    (0, typeorm_1.Column)({ type: 'float', default: 0 }),
    __metadata("design:type", Number)
], UserGames.prototype, "AverageCompletion", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Users_1.Users, users => users.UserGames),
    (0, typeorm_1.JoinColumn)({ name: 'UserId' }),
    __metadata("design:type", Users_1.Users)
], UserGames.prototype, "User", void 0);
__decorate([
    (0, typeorm_1.ManyToOne)(() => Games_1.Games, games => games.UserGames),
    (0, typeorm_1.JoinColumn)({ name: 'GameId' }),
    __metadata("design:type", Games_1.Games)
], UserGames.prototype, "Game", void 0);
exports.UserGames = UserGames = __decorate([
    (0, typeorm_1.Entity)(),
    (0, typeorm_1.Unique)(['User', 'Game'])
], UserGames);
