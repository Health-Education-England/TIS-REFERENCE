import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {TariffRate} from "./tariff-rate.model";
import {TariffRatePopupService} from "./tariff-rate-popup.service";
import {TariffRateService} from "./tariff-rate.service";
@Component({
	selector: 'jhi-tariff-rate-dialog',
	templateUrl: './tariff-rate-dialog.component.html'
})
export class TariffRateDialogComponent implements OnInit {

	tariffRate: TariffRate;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private tariffRateService: TariffRateService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['tariffRate']);
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
		if (this.tariffRate.id !== undefined) {
			this.tariffRateService.update(this.tariffRate)
				.subscribe((res: TariffRate) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.tariffRateService.create(this.tariffRate)
				.subscribe((res: TariffRate) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: TariffRate) {
		this.eventManager.broadcast({name: 'tariffRateListModification', content: 'OK'});
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
	selector: 'jhi-tariff-rate-popup',
	template: ''
})
export class TariffRatePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private tariffRatePopupService: TariffRatePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.tariffRatePopupService
					.open(TariffRateDialogComponent, params['id']);
			} else {
				this.modalRef = this.tariffRatePopupService
					.open(TariffRateDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
