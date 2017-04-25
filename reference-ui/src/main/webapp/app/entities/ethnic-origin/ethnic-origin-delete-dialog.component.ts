import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {EthnicOrigin} from "./ethnic-origin.model";
import {EthnicOriginPopupService} from "./ethnic-origin-popup.service";
import {EthnicOriginService} from "./ethnic-origin.service";

@Component({
	selector: 'jhi-ethnic-origin-delete-dialog',
	templateUrl: './ethnic-origin-delete-dialog.component.html'
})
export class EthnicOriginDeleteDialogComponent {

	ethnicOrigin: EthnicOrigin;

	constructor(private jhiLanguageService: JhiLanguageService,
				private ethnicOriginService: EthnicOriginService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['ethnicOrigin']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.ethnicOriginService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'ethnicOriginListModification',
				content: 'Deleted an ethnicOrigin'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-ethnic-origin-delete-popup',
	template: ''
})
export class EthnicOriginDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private ethnicOriginPopupService: EthnicOriginPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.ethnicOriginPopupService
				.open(EthnicOriginDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
