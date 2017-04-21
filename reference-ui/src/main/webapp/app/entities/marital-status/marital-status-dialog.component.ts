import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {MaritalStatus} from "./marital-status.model";
import {MaritalStatusPopupService} from "./marital-status-popup.service";
import {MaritalStatusService} from "./marital-status.service";
@Component({
	selector: 'jhi-marital-status-dialog',
	templateUrl: './marital-status-dialog.component.html'
})
export class MaritalStatusDialogComponent implements OnInit {

	maritalStatus: MaritalStatus;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private maritalStatusService: MaritalStatusService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['maritalStatus']);
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
		if (this.maritalStatus.id !== undefined) {
			this.maritalStatusService.update(this.maritalStatus)
				.subscribe((res: MaritalStatus) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.maritalStatusService.create(this.maritalStatus)
				.subscribe((res: MaritalStatus) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: MaritalStatus) {
		this.eventManager.broadcast({name: 'maritalStatusListModification', content: 'OK'});
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
	selector: 'jhi-marital-status-popup',
	template: ''
})
export class MaritalStatusPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private maritalStatusPopupService: MaritalStatusPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.maritalStatusPopupService
					.open(MaritalStatusDialogComponent, params['id']);
			} else {
				this.modalRef = this.maritalStatusPopupService
					.open(MaritalStatusDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
