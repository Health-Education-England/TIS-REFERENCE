export class Grade {
	constructor(public id?: number,
				public abbreviation?: string,
				public name?: string,
				public label?: string,
				public trainingGrade?: boolean,
				public postGrade?: boolean,
				public placementGrade?: boolean,) {
		this.trainingGrade = false;
		this.postGrade = false;
		this.placementGrade = false;
	}
}
