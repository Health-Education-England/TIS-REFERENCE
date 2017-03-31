import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {Gender} from "./gender.model";
import {GenderPopupService} from "./gender-popup.service";
import {GenderService} from "./gender.service";
@Component({
	selector: 'jhi-gender-dialog',
	templateUrl: './gender-dialog.component.html'
})
export class GenderDialogComponent implements OnInit {

	gender: Gender;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private genderService: GenderService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gender']);
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
		if (this.gender.id !== undefined) {
			this.genderService.update(this.gender)
				.subscribe((res: Gender) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.genderService.create(this.gender)
				.subscribe((res: Gender) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: Gender) {
		this.eventManager.broadcast({name: 'genderListModification', content: 'OK'});
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
	selector: 'jhi-gender-popup',
	template: ''
})
export class GenderPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private genderPopupService: GenderPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.genderPopupService
					.open(GenderDialogComponent, params['id']);
			} else {
				this.modalRef = this.genderPopupService
					.open(GenderDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
