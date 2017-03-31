import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {EthnicOrigin} from "./ethnic-origin.model";
import {EthnicOriginPopupService} from "./ethnic-origin-popup.service";
import {EthnicOriginService} from "./ethnic-origin.service";
@Component({
	selector: 'jhi-ethnic-origin-dialog',
	templateUrl: './ethnic-origin-dialog.component.html'
})
export class EthnicOriginDialogComponent implements OnInit {

	ethnicOrigin: EthnicOrigin;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private ethnicOriginService: EthnicOriginService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['ethnicOrigin']);
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
		if (this.ethnicOrigin.id !== undefined) {
			this.ethnicOriginService.update(this.ethnicOrigin)
				.subscribe((res: EthnicOrigin) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.ethnicOriginService.create(this.ethnicOrigin)
				.subscribe((res: EthnicOrigin) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: EthnicOrigin) {
		this.eventManager.broadcast({name: 'ethnicOriginListModification', content: 'OK'});
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
	selector: 'jhi-ethnic-origin-popup',
	template: ''
})
export class EthnicOriginPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private ethnicOriginPopupService: EthnicOriginPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.ethnicOriginPopupService
					.open(EthnicOriginDialogComponent, params['id']);
			} else {
				this.modalRef = this.ethnicOriginPopupService
					.open(EthnicOriginDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
