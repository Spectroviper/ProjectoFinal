import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Achievements } from "./Achievements";
import { Users } from "./Users";

@Entity()
@Unique(['Achievement', 'User'])
export class UserAchievements extends BaseEntity {

    @PrimaryGeneratedColumn()
    UserAchievementId!: number;

    @Column()
    Clear!: boolean;

    @Column({type: 'datetime', nullable: true})
    UnlockDate!: Date | null;

    @Column({nullable: true})
    TotalCollected!: number;

    @ManyToOne(() => Achievements, achievements => achievements.UserAchievements)
    @JoinColumn({ name: 'AchievementId' })
    Achievement!: Achievements;

    @ManyToOne(() => Users, users => users.UserAchievements)
    @JoinColumn({ name: 'UserId' })
    User!: Users;

}
