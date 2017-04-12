import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {FundingType} from "./funding-type.model";
import {FundingTypePopupService} from "./funding-type-popup.service";
import {FundingTypeService} from "./funding-type.service";
@Component({
	selector: 'jhi-funding-type-dialog',
	templateUrl: './funding-type-dialog.component.html'
})
export class FundingTypeDialogComponent implements OnInit {

	fundingType: FundingType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private fundingTypeService: FundingTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['fundingType']);
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
		if (this.fundingType.id !== undefined) {
			this.fundingTypeService.update(this.fundingType)
				.subscribe((res: FundingType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.fundingTypeService.create(this.fundingType)
				.subscribe((res: FundingType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: FundingType) {
		this.eventManager.broadcast({name: 'fundingTypeListModification', content: 'OK'});
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
	selector: 'jhi-funding-type-popup',
	template: ''
})
export class FundingTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingTypePopupService: FundingTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.fundingTypePopupService
					.open(FundingTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.fundingTypePopupService
					.open(FundingTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
