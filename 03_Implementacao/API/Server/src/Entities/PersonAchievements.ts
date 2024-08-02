import { BaseEntity, Column, Entity, JoinColumn, JoinTable, ManyToMany, ManyToOne, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Achievements } from "./Achievements";
import { Persons } from "./Persons";

@Entity()
export class PersonAchievements extends BaseEntity {

    @PrimaryGeneratedColumn()
    id!: number;

    @Column()
    Clear!: boolean;

    @Column({nullable: true})
    UnlockDate!: Date;

    @Column({nullable: true})
    TotalCollected!: string;

    @Column({nullable: true})
    UnlockedImage!: Buffer;

    @ManyToOne(() => Achievements, achievements => achievements.PersonAchievements)
    Achievement!: Achievements;

    @ManyToOne(() => Persons, persons => persons.PersonAchievements)
    @JoinColumn({ name: 'personId' })
    Person!: Persons;

}
