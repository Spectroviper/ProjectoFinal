import { BaseEntity, Column, Entity, JoinTable, ManyToMany, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Games } from "./Games";
import { PersonAchievements } from "./PersonAchievements";

@Entity()
export class Persons extends BaseEntity {

    @PrimaryGeneratedColumn()
    id!: number;

    @Column()
    Email!: string;

    @Column()
    UserName!: string;

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

    @Column()
    SiteRank!: number;

    @Column({nullable: true})
    Image!: string;

    @ManyToMany(() => Games, game => game.Players, {nullable: true})
    @JoinTable()
    Plays!: Games[];

    @OneToMany(() => PersonAchievements, personAchievement => personAchievement.Person, {nullable: true})
    PersonAchievements!: PersonAchievements[];
}