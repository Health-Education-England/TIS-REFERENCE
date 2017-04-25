import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Trust} from "./trust.model";
import {TrustPopupService} from "./trust-popup.service";
import {TrustService} from "./trust.service";

@Component({
	selector: 'jhi-trust-delete-dialog',
	templateUrl: './trust-delete-dialog.component.html'
})
export class TrustDeleteDialogComponent {

	trust: Trust;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trustService: TrustService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['trust']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.trustService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'trustListModification',
				content: 'Deleted an trust'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-trust-delete-popup',
	template: ''
})
export class TrustDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private trustPopupService: TrustPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.trustPopupService
				.open(TrustDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
