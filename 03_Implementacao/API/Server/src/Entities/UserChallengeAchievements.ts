import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Achievements } from "./Achievements";
import { Challenges } from "./Challenges";
import { Users } from "./Users";

@Entity()
@Unique(['User', 'Achievement', 'Challenge'])
export class UserChallengeAchievements extends BaseEntity {

    @PrimaryGeneratedColumn()
    UserChallengeAchievementId!: number;

    @Column()
    Clear!: boolean;

    @Column({type: 'datetime', nullable: true})
    UnlockDate!: Date | null;

    @Column({nullable: true})
    TotalCollected!: number;

    @ManyToOne(() => Achievements, achievements => achievements.UserChallengeAchievements)
    @JoinColumn({ name: 'AchievementId' })
    Achievement!: Achievements;

    @ManyToOne(() => Users, users => users.UserChallengeAchievements)
    @JoinColumn({ name: 'UserId' })
    User!: Users;

    @ManyToOne(() => Challenges, challenges => challenges.UserChallengeAchievements)
    @JoinColumn({ name: 'ChallengeId' })
    Challenge!: Challenges;

}