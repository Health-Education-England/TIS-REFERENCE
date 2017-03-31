import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {TariffRate} from "./tariff-rate.model";
import {TariffRatePopupService} from "./tariff-rate-popup.service";
import {TariffRateService} from "./tariff-rate.service";

@Component({
	selector: 'jhi-tariff-rate-delete-dialog',
	templateUrl: './tariff-rate-delete-dialog.component.html'
})
export class TariffRateDeleteDialogComponent {

	tariffRate: TariffRate;

	constructor(private jhiLanguageService: JhiLanguageService,
				private tariffRateService: TariffRateService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['tariffRate']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.tariffRateService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'tariffRateListModification',
				content: 'Deleted an tariffRate'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-tariff-rate-delete-popup',
	template: ''
})
export class TariffRateDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private tariffRatePopupService: TariffRatePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.tariffRatePopupService
				.open(TariffRateDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
