import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {MedicalSchool} from "./medical-school.model";
import {MedicalSchoolService} from "./medical-school.service";

@Component({
	selector: 'jhi-medical-school-detail',
	templateUrl: './medical-school-detail.component.html'
})
export class MedicalSchoolDetailComponent implements OnInit, OnDestroy {

	medicalSchool: MedicalSchool;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private medicalSchoolService: MedicalSchoolService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['medicalSchool']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.medicalSchoolService.find(id).subscribe(medicalSchool => {
			this.medicalSchool = medicalSchool;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
