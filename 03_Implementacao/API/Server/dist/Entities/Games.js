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
exports.Games = void 0;
const typeorm_1 = require("typeorm");
const Achievements_1 = require("./Achievements");
const UserGames_1 = require("./UserGames");
let Games = class Games extends typeorm_1.BaseEntity {
};
exports.Games = Games;
__decorate([
    (0, typeorm_1.PrimaryGeneratedColumn)(),
    __metadata("design:type", Number)
], Games.prototype, "GameId", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Games.prototype, "GameName", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Games.prototype, "About", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Games.prototype, "Console", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Games.prototype, "Developer", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Games.prototype, "Publisher", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", String)
], Games.prototype, "Genre", void 0);
__decorate([
    (0, typeorm_1.Column)(),
    __metadata("design:type", Date)
], Games.prototype, "ReleaseDate", void 0);
__decorate([
    (0, typeorm_1.Column)({ nullable: true }),
    __metadata("design:type", String)
], Games.prototype, "Image", void 0);
__decorate([
    (0, typeorm_1.Column)({ default: "system" }),
    __metadata("design:type", String)
], Games.prototype, "CreatedBy", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => Achievements_1.Achievements, achievements => achievements.Game, { nullable: true }),
    __metadata("design:type", Array)
], Games.prototype, "Achievements", void 0);
__decorate([
    (0, typeorm_1.OneToMany)(() => UserGames_1.UserGames, userGames => userGames.Game, { nullable: true }),
    __metadata("design:type", Array)
], Games.prototype, "UserGames", void 0);
exports.Games = Games = __decorate([
    (0, typeorm_1.Entity)()
], Games);
