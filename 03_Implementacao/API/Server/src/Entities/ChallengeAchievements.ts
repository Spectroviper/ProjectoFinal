import { BaseEntity, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Achievements } from "./Achievements";
import { Challenges } from "./Challenges";

@Entity()
@Unique(['Achievement', 'Challenge'])
export class ChallengeAchievements extends BaseEntity {

    @PrimaryGeneratedColumn()
    ChallengeAchievementId!: number;

    @ManyToOne(() => Achievements, achievements => achievements.ChallengeAchievements)
    @JoinColumn({ name: 'AchievementId' })
    Achievement!: Achievements;

    @ManyToOne(() => Challenges, challenges => challenges.ChallengeAchievements)
    @JoinColumn({ name: 'ChallengeId' })
    Challenge!: Challenges;

}