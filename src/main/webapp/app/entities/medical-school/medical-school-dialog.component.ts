import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {MedicalSchool} from "./medical-school.model";
import {MedicalSchoolPopupService} from "./medical-school-popup.service";
import {MedicalSchoolService} from "./medical-school.service";
@Component({
	selector: 'jhi-medical-school-dialog',
	templateUrl: './medical-school-dialog.component.html'
})
export class MedicalSchoolDialogComponent implements OnInit {

	medicalSchool: MedicalSchool;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private medicalSchoolService: MedicalSchoolService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['medicalSchool']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.medicalSchool.id !== undefined) {
			this.medicalSchoolService.update(this.medicalSchool)
				.subscribe((res: MedicalSchool) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.medicalSchoolService.create(this.medicalSchool)
				.subscribe((res: MedicalSchool) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: MedicalSchool) {
		this.eventManager.broadcast({name: 'medicalSchoolListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-medical-school-popup',
	template: ''
})
export class MedicalSchoolPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private medicalSchoolPopupService: MedicalSchoolPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.medicalSchoolPopupService
					.open(MedicalSchoolDialogComponent, params['id']);
			} else {
				this.modalRef = this.medicalSchoolPopupService
					.open(MedicalSchoolDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
