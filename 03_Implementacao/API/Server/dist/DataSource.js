"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
var _a, _b, _c, _d;
Object.defineProperty(exports, "__esModule", { value: true });
exports.appDataSource = void 0;
const typeorm_1 = require("typeorm");
const dotenv_1 = __importDefault(require("dotenv"));
const Users_1 = require("./Entities/Users");
const Games_1 = require("./Entities/Games");
const Achievements_1 = require("./Entities/Achievements");
const UserAchievements_1 = require("./Entities/UserAchievements");
const Challenges_1 = require("./Entities/Challenges");
const ChallengeAchievements_1 = require("./Entities/ChallengeAchievements");
const ChallengeInvites_1 = require("./Entities/ChallengeInvites");
const UserChallengeAchievements_1 = require("./Entities/UserChallengeAchievements");
const UserChallenges_1 = require("./Entities/UserChallenges");
const UserGames_1 = require("./Entities/UserGames");
const Friends_1 = require("./Entities/Friends");
dotenv_1.default.config();
exports.appDataSource = new typeorm_1.DataSource({
    type: 'mysql',
    host: (_a = process.env.DB_HOST) !== null && _a !== void 0 ? _a : 'localhost',
    username: (_b = process.env.MYSQL_USER) !== null && _b !== void 0 ? _b : 'admin',
    password: (_c = process.env.MYSQL_PASSWORD) !== null && _c !== void 0 ? _c : 'admin1234',
    database: (_d = process.env.MYSQL_DATABASE) !== null && _d !== void 0 ? _d : 'admin',
    port: Number(process.env.MYSQL_PORT) || 3306,
    logging: true,
    synchronize: true,
    entities: [
        Users_1.Users,
        Games_1.Games,
        Achievements_1.Achievements,
        UserAchievements_1.UserAchievements,
        Challenges_1.Challenges,
        ChallengeAchievements_1.ChallengeAchievements,
        ChallengeInvites_1.ChallengeInvites,
        UserChallengeAchievements_1.UserChallengeAchievements,
        UserChallenges_1.UserChallenges,
        UserGames_1.UserGames,
        Friends_1.Friends
    ],
});
