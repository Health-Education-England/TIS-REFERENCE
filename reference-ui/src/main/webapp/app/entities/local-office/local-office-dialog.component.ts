import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {LocalOffice} from "./local-office.model";
import {LocalOfficePopupService} from "./local-office-popup.service";
import {LocalOfficeService} from "./local-office.service";
@Component({
	selector: 'jhi-local-office-dialog',
	templateUrl: './local-office-dialog.component.html'
})
export class LocalOfficeDialogComponent implements OnInit {

	localOffice: LocalOffice;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private localOfficeService: LocalOfficeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['localOffice']);
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
		if (this.localOffice.id !== undefined) {
			this.localOfficeService.update(this.localOffice)
				.subscribe((res: LocalOffice) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.localOfficeService.create(this.localOffice)
				.subscribe((res: LocalOffice) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: LocalOffice) {
		this.eventManager.broadcast({name: 'localOfficeListModification', content: 'OK'});
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
	selector: 'jhi-local-office-popup',
	template: ''
})
export class LocalOfficePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private localOfficePopupService: LocalOfficePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.localOfficePopupService
					.open(LocalOfficeDialogComponent, params['id']);
			} else {
				this.modalRef = this.localOfficePopupService
					.open(LocalOfficeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
