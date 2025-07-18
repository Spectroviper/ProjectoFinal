import { BaseEntity, Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn, Unique } from "typeorm";
import { Challenges } from "./Challenges";
import { Users } from "./Users";

@Entity()
@Unique(['Challenge', 'User'])
export class ChallengeInvites extends BaseEntity {

    @PrimaryGeneratedColumn()
    ChallengeInviteId!: number;

    @Column({default: "Pending"})
    State!: string;

    @Column()
    Date!: Date;

    @Column()
    IsRequest!: boolean;

    @ManyToOne(() => Challenges, challenges => challenges.ChallengeInvites)
    @JoinColumn({ name: 'ChallengeId' })
    Challenge!: Challenges;

    @ManyToOne(() => Users, users => users.ChallengeInvites)
    @JoinColumn({ name: 'UserId' })
    User!: Users;

}