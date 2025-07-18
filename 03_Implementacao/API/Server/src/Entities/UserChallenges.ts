import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Challenges } from "./Challenges";
import { Users } from "./Users";

@Entity()
@Unique(['Challenge', 'User'])
export class UserChallenges extends BaseEntity {

    @PrimaryGeneratedColumn()
    UserChallengeId!: number;

    @Column()
    IsLeader!: boolean;

    @Column({ type: 'float', default: 0 })
    AverageCompletion!: number;

    @ManyToOne(() => Challenges, challenges => challenges.UserChallenges)
    @JoinColumn({ name: 'ChallengeId' })
    Challenge!: Challenges;

    @ManyToOne(() => Users, users => users.UserChallenges)
    @JoinColumn({ name: 'UserId' })
    User!: Users;

}