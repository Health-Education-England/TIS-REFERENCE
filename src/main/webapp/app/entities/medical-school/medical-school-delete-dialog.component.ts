import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {MedicalSchool} from "./medical-school.model";
import {MedicalSchoolPopupService} from "./medical-school-popup.service";
import {MedicalSchoolService} from "./medical-school.service";

@Component({
	selector: 'jhi-medical-school-delete-dialog',
	templateUrl: './medical-school-delete-dialog.component.html'
})
export class MedicalSchoolDeleteDialogComponent {

	medicalSchool: MedicalSchool;

	constructor(private jhiLanguageService: JhiLanguageService,
				private medicalSchoolService: MedicalSchoolService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['medicalSchool']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.medicalSchoolService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'medicalSchoolListModification',
				content: 'Deleted an medicalSchool'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-medical-school-delete-popup',
	template: ''
})
export class MedicalSchoolDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private medicalSchoolPopupService: MedicalSchoolPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.medicalSchoolPopupService
				.open(MedicalSchoolDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
