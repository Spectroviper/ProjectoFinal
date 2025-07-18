import { BaseEntity, Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { ChallengeInvites } from "./ChallengeInvites";
import {UserAchievements } from "./UserAchievements";
import { UserChallengeAchievements } from "./UserChallengeAchievements";
import { UserChallenges } from "./UserChallenges";
import { UserGames } from "./UserGames";
import { Friends } from "./Friends";

@Entity()
export class Users extends BaseEntity {

    @PrimaryGeneratedColumn()
    UserId!: number;

    @Column({unique: true})
    UserName!: string;

    @Column({unique: true})
    Email!: string;

    @Column({nullable: true})
    Biography!: string;

    @Column()
    MemberSince!: Date;
    
    @Column()
    LastLogin!: Date;

    @Column()
    TotalPoints!: number;

    @Column()
    AverageCompletion!: number;

    @Column({nullable: true})
    Image!: string;

    @OneToMany(() => UserAchievements, UserAchievement => UserAchievement.User, {nullable: true})
    UserAchievements!: UserAchievements[];

    @OneToMany(() => UserChallengeAchievements, UserChallengeAchievement => UserChallengeAchievement.User, {nullable: true})
    UserChallengeAchievements!: UserChallengeAchievements[];

    @OneToMany(() => UserChallenges, UserChallenge => UserChallenge.User, {nullable: true})
    UserChallenges!: UserChallenges[];

    @OneToMany(() => ChallengeInvites, ChallengeInvite => ChallengeInvite.User, {nullable: true})
    ChallengeInvites!: ChallengeInvites[];

    @OneToMany(() => UserGames, UserGame => UserGame.User, {nullable: true})
    UserGames!: UserGames[];

    @OneToMany(() => Friends, FriendSender => FriendSender.UserSender, {nullable: true})
    FriendSenders!: Friends[];

    @OneToMany(() => Friends, FriendReceiver => FriendReceiver.UserReceiver, {nullable: true})
    FriendReceivers!: Friends[];
}