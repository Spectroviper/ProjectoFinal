import { DataSource } from 'typeorm';
import dotenv from 'dotenv';
import { Users } from './Entities/Users';
import { Games } from './Entities/Games';
import { Achievements } from './Entities/Achievements';
import { UserAchievements } from './Entities/UserAchievements';
import { Challenges } from './Entities/Challenges';
import { ChallengeAchievements } from './Entities/ChallengeAchievements';
import { ChallengeInvites } from './Entities/ChallengeInvites';
import { UserChallengeAchievements } from './Entities/UserChallengeAchievements';
import { UserChallenges } from './Entities/UserChallenges';
import { UserGames } from './Entities/UserGames';
import { Friends } from './Entities/Friends';

dotenv.config();

export const appDataSource = new DataSource({
  type: 'mysql',
  host: process.env.DB_HOST ?? 'localhost',
  username: process.env.MYSQL_USER ?? 'admin',
  password: process.env.MYSQL_PASSWORD?? 'admin1234',
  database: process.env.MYSQL_DATABASE ?? 'admin',
  port: Number(process.env.MYSQL_PORT) || 3306,
  logging: true,
  synchronize: true,
  entities: [
    Users,
    Games,
    Achievements,
    UserAchievements,
    Challenges,
    ChallengeAchievements,
    ChallengeInvites,
    UserChallengeAchievements,
    UserChallenges,
    UserGames,
    Friends
  ],
});
