import { BaseEntity, Column, Entity, JoinTable, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Games } from "./Games";
import { PersonAchievements } from "./PersonAchievements";

@Entity()
export class Achievements extends BaseEntity {

    @PrimaryGeneratedColumn()
    id!: number;

    @Column()
    AchievementName!: string;

    @Column({nullable: true})
    About!: string;

    @Column()
    RetroPoints!: number;
    
    @Column({nullable: true})
    TotalCollectable!: number;

    @Column({nullable: true})
    Image!: string;

    @ManyToOne(() => Games, games => games.Achievements)
    Game!: Games;

    @OneToMany(() => PersonAchievements, personAchievement => personAchievement.Achievement)
    PersonAchievements!: PersonAchievements[];
}