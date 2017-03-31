import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {FundingType} from "./funding-type.model";
import {FundingTypePopupService} from "./funding-type-popup.service";
import {FundingTypeService} from "./funding-type.service";

@Component({
	selector: 'jhi-funding-type-delete-dialog',
	templateUrl: './funding-type-delete-dialog.component.html'
})
export class FundingTypeDeleteDialogComponent {

	fundingType: FundingType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingTypeService: FundingTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['fundingType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.fundingTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'fundingTypeListModification',
				content: 'Deleted an fundingType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-funding-type-delete-popup',
	template: ''
})
export class FundingTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingTypePopupService: FundingTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.fundingTypePopupService
				.open(FundingTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
