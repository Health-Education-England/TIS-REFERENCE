import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {InactiveReason} from "./inactive-reason.model";
import {InactiveReasonPopupService} from "./inactive-reason-popup.service";
import {InactiveReasonService} from "./inactive-reason.service";
@Component({
	selector: 'jhi-inactive-reason-dialog',
	templateUrl: './inactive-reason-dialog.component.html'
})
export class InactiveReasonDialogComponent implements OnInit {

	inactiveReason: InactiveReason;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private inactiveReasonService: InactiveReasonService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['inactiveReason']);
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
		if (this.inactiveReason.id !== undefined) {
			this.inactiveReasonService.update(this.inactiveReason)
				.subscribe((res: InactiveReason) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.inactiveReasonService.create(this.inactiveReason)
				.subscribe((res: InactiveReason) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: InactiveReason) {
		this.eventManager.broadcast({name: 'inactiveReasonListModification', content: 'OK'});
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
	selector: 'jhi-inactive-reason-popup',
	template: ''
})
export class InactiveReasonPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private inactiveReasonPopupService: InactiveReasonPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.inactiveReasonPopupService
					.open(InactiveReasonDialogComponent, params['id']);
			} else {
				this.modalRef = this.inactiveReasonPopupService
					.open(InactiveReasonDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
