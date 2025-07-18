import { BaseEntity, Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { ChallengeAchievements } from "./ChallengeAchievements";
import { ChallengeInvites } from "./ChallengeInvites";
import { UserChallengeAchievements } from "./UserChallengeAchievements";
import { UserChallenges } from "./UserChallenges";

@Entity()
export class Challenges extends BaseEntity {

    @PrimaryGeneratedColumn()
    ChallengeId!: number;

    @Column()
    ChallengeName!: string;

    @Column({default: "endfirst"})
    Type!: string;

    @Column({default: false})
    IsStarted!: boolean;

    @Column({nullable: true})
    StartDate!: Date;
    
    @Column({nullable: true})
    EndDate!: Date;

    @Column({nullable: true})
    Image!: string;

    @Column()
    CreatedBy!: string;

    @OneToMany(() => UserChallenges, UserChallenge => UserChallenge.Challenge)
    UserChallenges!: UserChallenges[];

    @OneToMany(() => ChallengeInvites, ChallengeInvite => ChallengeInvite.Challenge, {nullable: true})
    ChallengeInvites!: ChallengeInvites[];

    @OneToMany(() => UserChallengeAchievements, UserChallengeAchievement => UserChallengeAchievement.Challenge)
    UserChallengeAchievements!: UserChallengeAchievements[];

    @OneToMany(() => ChallengeAchievements, ChallengeAchievement => ChallengeAchievement.Challenge)
    ChallengeAchievements!: ChallengeAchievements[];

}