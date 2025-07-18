import { BaseEntity, Column, Entity, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { ChallengeAchievements } from "./ChallengeAchievements";
import { Games } from "./Games";
import { UserAchievements } from "./UserAchievements";
import { UserChallengeAchievements } from "./UserChallengeAchievements";

@Entity()
export class Achievements extends BaseEntity {

    @PrimaryGeneratedColumn()
    AchievementId!: number;

    @Column()
    AchievementName!: string;

    @Column({nullable: true})
    About!: string;
    
    @Column({nullable: true})
    TotalCollectable!: number;

    @Column({nullable: true})
    Image!: string;

    @Column()
    CreatedBy!: string;

    @ManyToOne(() => Games, games => games.Achievements)
    Game!: Games;

    @OneToMany(() => UserAchievements, UserAchievement => UserAchievement.Achievement, {nullable: true})
    UserAchievements!: UserAchievements[];

    @OneToMany(() => ChallengeAchievements, ChallengeAchievement => ChallengeAchievement.Achievement, {nullable: true})
    ChallengeAchievements!: ChallengeAchievements[];

    @OneToMany(() => UserChallengeAchievements, UserChallengeAchievement => UserChallengeAchievement.Achievement, {nullable: true})
    UserChallengeAchievements!: UserChallengeAchievements[];
}